package com.blogapp.common.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

@Slf4j
@Service
public class CsvExportService {

    /**
     * A highly decoupled, highly reusable generic CSV export utility.
     * This method does not know anything about the database. You simply pass it data, and it streams it to the user.
     *
     * @param response  The raw HTTP response object to stream the file to
     * @param filename  The name of the downloaded file (e.g., "users_export.csv")
     * @param headers   An array of string column names
     * @param data      The raw dataset (can be List<Entity>, List<Map>, etc.)
     * @param rowMapper A pure function that tells the engine how to convert 1 entity into 1 array of strings
     * @param <T>       The generic type of your data
     */
    public <T> void exportCsvToResponse(HttpServletResponse response,
                                        String filename,
                                        String[] headers,
                                        Iterable<T> data,
                                        Function<T, String[]> rowMapper) {

        // 1. Set exactly the correct HTTP headers for a file download
        response.setContentType("text/csv");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

        try (PrintWriter writer = response.getWriter()) {
            
            // Write the UTF-8 BOM so Microsoft Excel renders international characters correctly
            writer.write('\ufeff');

            // 2. Write Headers
            writeCsvRow(writer, headers);

            // 3. Stream Data Rows
            for (T item : data) {
                String[] row = rowMapper.apply(item);
                writeCsvRow(writer, row);
            }

            writer.flush();
            log.info("Successfully exported generic CSV file: {}", filename);

        } catch (IOException e) {
            log.error("Failed to stream CSV data to response for file: {}", filename, e);
            throw new RuntimeException("CSV Export failed securely", e);
        }
    }

    /**
     * Helper cleanly formats an array of strings into a memory-safe CSV row.
     * It actively escapes embedded quotes, commas, and newlines.
     */
    private void writeCsvRow(PrintWriter writer, String[] values) {
        if (values == null || values.length == 0) {
            writer.println();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            String value = values[i] == null ? "" : values[i];
            
            // If the value contains quotes, commas, or newlines, wrap it in double-quotes and escape internal quotes
            boolean requiresEscaping = value.contains("\"") || value.contains(",") || value.contains("\n") || value.contains("\r");
            if (requiresEscaping) {
                sb.append("\"").append(value.replace("\"", "\"\"")).append("\"");
            } else {
                sb.append(value);
            }

            if (i < values.length - 1) {
                sb.append(",");
            }
        }
        writer.println(sb.toString());
    }
}

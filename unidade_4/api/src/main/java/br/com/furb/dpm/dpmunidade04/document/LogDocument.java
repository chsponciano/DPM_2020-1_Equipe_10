package br.com.furb.dpm.dpmunidade04.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "log")
public class LogDocument {

    public static final String INTERNAL_LOG = "Usuário: %s | %s | Data/Hora: %s";
    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Id
    String id;
    String log;

    public static String getCurrentDatetime() {
        return sdf.format(new Date());
    }
}

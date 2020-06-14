package br.com.furb.dpm.dpmunidade04.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Document(collection = "user")
public class UserDocument {

    @Id
    String id;
    String name;
    String email;
    String username;
    String password;

}

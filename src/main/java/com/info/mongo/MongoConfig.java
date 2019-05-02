//package com.info.mongo;
//
//import com.mongodb.*;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
///**
// * @Author: kiro
// * @Date: 2018/7/2
// * @Description:
// */
//@Slf4j
//@Configuration
//@EnableMongoRepositories
//public class MongoConfig extends AbstractMongoConfiguration {
//    public static final int DEFAULT_PORT = 27017;
//    public static final String DEFAULT_URI = "mongodb://localhost/test";
//
//    /**
//     * Mongo server host. Cannot be set with uri.
//     */
//    @Value("#{propSettings['spring.data.mongodb.host']}")
//    private String host;
//    /**
//     * Mongo server port. Cannot be set with uri.
//     */
//    @Value("#{propSettings['spring.data.mongodb.port']}")
//    private Integer port;
//    /**
//     * Mongo database URI. Cannot be set with host, port and credentials.
//     */
//    @Value("#{propSettings['spring.data.mongodb.uri']}")
//    private String uri;
//    /**
//     * Database name.
//     */
//    @Value("#{propSettings['spring.data.mongodb.database']}")
//    private String database;
//    /**
//     * Authentication database name.
//     */
//    @Value("#{propSettings['spring.data.mongodb.authentication-database']}")
//    private String authenticationDatabase;
//    /**
//     * Login user of the mongo server. Cannot be set with uri.
//     */
//    @Value("#{propSettings['spring.data.mongodb.username']}")
//    private String username;
//    /**
//     * Login password of the mongo server. Cannot be set with uri.
//     */
//    @Value("#{propSettings['spring.data.mongodb.password']}")
//    private String password;
//
//    private boolean hasCustomAddress() {
//        return this.host != null || this.port != null;
//    }
//
//    private boolean hasCustomCredentials() {
//        return this.username != null && this.password != null;
//    }
//
//    public void clearPassword() {
//        if (this.password == null) {
//            return;
//        }
//        this.password = null;
//    }
//
//
//    public String determineUri() {
//        return (this.uri != null ? this.uri : DEFAULT_URI);
//    }
//
//    public String getMongoClientDatabase() {
//        if (this.database != null) {
//            return this.database;
//        }
//        return new MongoClientURI(determineUri()).getDatabase();
//    }
//
//    private int determinePort() {
//        if (this.port == null) {
//            return DEFAULT_PORT;
//        }
//        return this.port;
//    }
//
//    @Override
//    protected String getDatabaseName() {
//        return getMongoClientDatabase();
//    }
//
//    @Override
//    public Mongo mongo() {
//        try {
//
//            if (hasCustomAddress() || hasCustomCredentials()) {
//                if (this.uri != null) {
//                    throw new IllegalStateException("Invalid mongo configuration, "
//                            + "either uri or host/port/credentials must be specified");
//                }
//                List<MongoCredential> credentials = new ArrayList<>();
//                if (hasCustomCredentials()) {
//                    String database = this.authenticationDatabase == null
//                            ? getMongoClientDatabase() : this.authenticationDatabase;
//                    credentials.add(MongoCredential.createCredential(this.username,
//                            database, this.password.toCharArray()));
//                }
//                String host = this.host == null ? "localhost" : this.host;
//                int port = determinePort();
//                MongoClientOptions options = MongoClientOptions.builder().build();
//                return new MongoClient(
//                        Collections.singletonList(new ServerAddress(host, port)),
//                        credentials, options);
//            }
//            MongoClientOptions options = MongoClientOptions.builder().build();
//            // The options and credentials are in the URI
//            return new MongoClient(new MongoClientURI(determineUri(), builder(options)));
//        } finally {
//            clearPassword();
//        }
//    }
//
//    private MongoClientOptions.Builder builder(MongoClientOptions options) {
//        if (options != null) {
//            return MongoClientOptions.builder(options);
//        }
//        return MongoClientOptions.builder();
//    }
//}

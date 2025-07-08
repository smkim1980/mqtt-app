DROP TABLE IF EXISTS ENCRYPTED_LOCATION;

CREATE TABLE ENCRYPTED_LOCATION (
                                    device_id VARCHAR(255),
                                    vehicle_id VARCHAR(255),
                                    encrypted_latitude VARCHAR(255),
                                    encrypted_longitude VARCHAR(255),
                                    heading REAL,
                                    altitude REAL,
                                    speed REAL,
                                    occur_dt BIGINT,
                                    route_dir VARCHAR(255),
                                    last_stop_id VARCHAR(255),
                                    last_stop_seq BIGINT,
                                    last_stop_dist BIGINT,
                                    origin_accum_dist BIGINT,
                                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
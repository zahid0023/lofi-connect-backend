-- Table to store app keys for your system

CREATE TABLE lofi_connect_app_key (
id BIGSERIAL PRIMARY KEY,
app_key text KEY,
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
created_by VARCHAR(255),
updated_by VARCHAR(255)
);

-- Table to store GoHighLevel tokens linked to an app key
CREATE TABLE go_high_level_tokens (
id BIGSERIAL PRIMARY KEY,
app_key_id SERIAL NOT NULL,
access_token TEXT NOT NULL,
token_type VARCHAR(50),
expires_in INT,
refresh_token TEXT,
refresh_token_id VARCHAR(255),
scopes TEXT,docker
is_location BOOLEAN,
company_id VARCHAR(255),
location_id VARCHAR(255),
user_id VARCHAR(255),
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
FOREIGN KEY (app_key_id) REFERENCES lofi_connect_app_key(id) ON DELETE CASCADE
);

1. lofi_connect_app_key.app_key is the unique identifier we provide to our clients.

2. go_high_level_tokens.app_key_id links each token to a specific app key.

3. TEXT type is used for access_token and refresh_token because they can be very long.

4. created_at and updated_at track record timestamps.

5. ON DELETE CASCADE ensures that when an app key is deleted, its tokens are also deleted.

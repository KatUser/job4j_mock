ALTER TABLE IF EXISTS interview
    add constraint submitter_id
        FOREIGN KEY (submitter_id) REFERENCES submitter (id)
            ON DELETE CASCADE
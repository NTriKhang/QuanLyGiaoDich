--- Khang ---- Sign up

CREATE OR REPLACE FUNCTION create_new_db_user (
    p_username IN VARCHAR2,
    p_password IN VARCHAR2
) RETURN NUMBER
IS
BEGIN
    EXECUTE IMMEDIATE 'CREATE USER ' || p_username || ' IDENTIFIED BY ' || p_password;
    
    EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE TO ' || p_username;
    
    COMMIT;
    
    RETURN 1; -- Success
END;


CREATE OR REPLACE PROCEDURE create_new_application_user(
    p_user_id IN VARCHAR2,
    p_first_name IN VARCHAR2,
    p_last_name IN VARCHAR2,
    p_address IN VARCHAR2,
    p_phone IN VARCHAR2,
    p_email IN VARCHAR2,
    p_user_name IN VARCHAR2,
    p_password IN VARCHAR2,
    p_image_profile IN BLOB,
    p_result OUT NUMBER
)
IS
    v_user_count NUMBER;
    v_created_date TIMESTAMP;
BEGIN
    -- Check if the username already exists
    SELECT COUNT(*)
    INTO v_user_count
    FROM all_users
    WHERE username = UPPER(p_user_name); -- Use UPPER for case-insensitive comparison
   
    IF v_user_count = 0 THEN
        -- Get the current timestamp
        SELECT CAST(SYSTIMESTAMP AS TIMESTAMP) INTO v_created_date FROM DUAL;

        INSERT INTO users
        VALUES (p_user_id, p_first_name, p_last_name, p_address, p_phone, p_email, p_user_name, p_password, p_image_profile, 0, v_created_date, NULL);
        
        DECLARE user_db_res NUMBER;
        BEGIN
            user_db_res := create_new_db_user(p_user_name, p_password);
            IF user_db_res = 1 THEN    
                p_result := 200; -- Success http code
            ELSE 
                p_result := 400; -- Bad request http code
            END IF;
        END;
    ELSE
        p_result := 409; -- Conflict http code (username already exists)
    END IF;
END;
/
-- test
DECLARE
    v_results NUMBER;
BEGIN
    DBMS_OUTPUT.PUT_LINE('Testing...');
    create_new_application_user('001', 'A', 'S', 'ADD', '123', 'MAIL', 'S', 'AS', NULL, v_results);
    DBMS_OUTPUT.PUT_LINE('Result: ' || v_results);
END;

--------------------------------------------------------------------------------------------
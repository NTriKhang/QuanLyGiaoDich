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
-----Kiet---------
SET SERVEROUTPUT ON
-----xemtablespace-------
create or replace NONEDITIONABLE PROCEDURE GET_TABLESPACE_INFO(p_cur OUT SYS_REFCURSOR) IS
BEGIN
    OPEN p_cur FOR
    SELECT file_name,
           bytes/1024/1024 AS size_mb,
           tablespace_name
    FROM dba_data_files;
END;

-----------taotablespace----------
create or replace NONEDITIONABLE PROCEDURE create_tablespace_user_choice(
    p_tablespace_name IN VARCHAR2,
    p_datafile_path IN VARCHAR2,
    p_datafile_size IN NUMBER
) IS
    v_sql VARCHAR2(4000);
BEGIN
    v_sql := 'CREATE TABLESPACE ' || p_tablespace_name ||
             ' DATAFILE ''' || p_datafile_path || ''' SIZE ' || p_datafile_size || 'M' ||
             ' AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED';
    EXECUTE IMMEDIATE v_sql;
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END create_tablespace_user_choice;

-------timkiem--------------
create or replace NONEDITIONABLE PROCEDURE get_user_tablespaces(
    p_username IN VARCHAR2,
    p_recordset OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN p_recordset FOR
        SELECT df.file_name, 
               df.bytes/1024/1024 AS size, 
               ts.tablespace_name
        FROM dba_data_files df
        JOIN dba_tablespaces ts ON df.tablespace_name = ts.tablespace_name
        WHERE ts.tablespace_name IN (
            SELECT default_tablespace 
            FROM dba_users 
            WHERE username = UPPER(p_username)
        );
END get_user_tablespaces;

---------themdatafile------------
create or replace NONEDITIONABLE PROCEDURE add_datafile_to_tablespace (
    p_tablespace_name IN VARCHAR2,
    p_datafile_path IN VARCHAR2,
    p_datafile_size IN NUMBER
) IS
BEGIN
    EXECUTE IMMEDIATE 'ALTER TABLESPACE ' || p_tablespace_name || 
                     ' ADD DATAFILE ''' || p_datafile_path || ''' SIZE ' || 
                     p_datafile_size || 'M AUTOEXTEND ON NEXT 10M MAXSIZE UNLIMITED';
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('An error occurred: ' || SQLERRM);
END add_datafile_to_tablespace;


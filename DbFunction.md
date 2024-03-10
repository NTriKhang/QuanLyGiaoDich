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


----- Hieu ----
CREATE OR REPLACE FUNCTION get_sga_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT * FROM V$SGA;
    RETURN v_cursor;
END;

create or replace FUNCTION get_pga_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT * FROM v$pgastat;
    RETURN v_cursor;
END;

create or replace FUNCTION get_process_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT pid, spid, program FROM v$process;
    RETURN v_cursor;
END;

create or replace FUNCTION get_datafile_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT file_name, file_id, tablespace_name, bytes, status FROM dba_data_files;
    RETURN v_cursor;
END;

create or replace FUNCTION get_instance_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    select instance_number, instance_name, version, startup_time, status, database_status from v$instance;
    RETURN v_cursor;
END;

create or replace FUNCTION get_controlfile_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT status, name FROM v$controlfile;
    RETURN v_cursor;
END;

create or replace FUNCTION get_spfile_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT value, name FROM V$PARAMETER WHERE name = 'spfile';
    RETURN v_cursor;
END;

create or replace FUNCTION get_database_info
return SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    select dbid, name, created, open_mode, log_mode, controlfile_type from v$database;
    RETURN v_cursor;
END;
-----------------------------------------------------------------
V2
-----------Khang--------
Logout
create or replace PROCEDURE logout(username_in VARCHAR2, p_result OUT NUMBER) IS
    v_sid INTEGER;
    v_serial INTEGER;
BEGIN
    p_result := 500;
    SELECT sid, serial#
    INTO v_sid, v_serial
    FROM v$session
    WHERE username = upper(username_in);
    DBMS_OUTPUT.PUT_LINE(v_sid);
    DBMS_OUTPUT.PUT_LINE(v_serial);
    EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || v_sid || ',' || v_serial || ''' IMMEDIATE';

    p_result := 200;
END;

check if user've already signed in or not
create or replace PROCEDURE check_user_signin(username_in VARCHAR2, p_result OUT NUMBER) IS
    v_count INTEGER;
BEGIN
    SELECT count(*)
    INTO v_count
    FROM v$session
    WHERE type != 'BACKGROUND' AND program LIKE '%JDBC Thin Client%' AND USERNAME = upper(username_in);
    IF v_count > 0 THEN
        p_result := 200;
    ELSE
        p_result := 409;
    END IF;
END;

CREATE OR REPLACE FUNCTION get_session_info
RETURN SYS_REFCURSOR
IS
   v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor for
    SELECT sid, serial#, username, program from v$session where type!='BACKGROUND';
    Return v_cursor;
END;

CREATE OR REPLACE PROCEDURE kill_session (
    p_sid IN NUMBER,
    p_serial IN NUMBER
)
IS
BEGIN
    EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || p_sid || ',' || p_serial || ''' IMMEDIATE';
END;

------------------------------------Kiệt
create or replace NONEDITIONABLE FUNCTION get_user_tablespaces_info(p_username IN VARCHAR2)
RETURN SYS_REFCURSOR IS
  v_cursor SYS_REFCURSOR;
BEGIN
  OPEN v_cursor FOR
    SELECT df.file_name, df.bytes/1024/1024 AS size_in_mb, ts.tablespace_name
    FROM dba_data_files df
    JOIN dba_tablespaces ts ON df.tablespace_name = ts.tablespace_name
    WHERE EXISTS (SELECT 1 FROM dba_users u WHERE u.default_tablespace = ts.tablespace_name AND u.username = UPPER(p_username));
  RETURN v_cursor;
END;
------------v3
Khang
tạo profile
-- create profile
CREATE PROFILE DW_PROFILE LIMIT
SESSIONS_PER_USER 2
IDLE_TIME 1
CONNECT_TIME 10;

lấy process tương ứng với session
CREATE OR REPLACE FUNCTION get_process_info(session_id IN NUMBER) RETURN SYS_REFCURSOR IS
    process_info SYS_REFCURSOR;
BEGIN
    OPEN process_info FOR
        SELECT p.spid AS OS_Process_ID, s.paddr as address,
        p.PGA_USED_MEM, PGA_ALLOC_MEM,PGA_FREEABLE_MEM, PGA_MAX_MEM
        FROM   v$process p 
        JOIN   v$session s ON p.addr = s.paddr 
        WHERE  s.sid=session_id;     
    RETURN process_info;
END;


thêm logout all, kill tất cả các session
create or replace PROCEDURE logout_all(username_in VARCHAR2, p_result OUT NUMBER) IS
BEGIN
    p_result := 500;
    FOR session_rec IN (SELECT sid, serial# FROM v$session WHERE username = upper(username_in)) LOOP
        EXECUTE IMMEDIATE 'ALTER SYSTEM KILL SESSION ''' || session_rec.sid || ',' || session_rec.serial# || ''' IMMEDIATE';
    END LOOP;

    p_result := 200;
END;
-- chỉnh lại đăng ký user, cấp thêm quyền select, insert
create or replace FUNCTION create_new_db_user (
    p_username IN VARCHAR2,
    p_password IN VARCHAR2
) RETURN NUMBER
IS
BEGIN
    EXECUTE IMMEDIATE 'CREATE USER ' || p_username || ' IDENTIFIED BY ' || p_password || ' PROFILE DW_PROFILE';

    EXECUTE IMMEDIATE 'GRANT CONNECT, RESOURCE TO ' || p_username;
    EXECUTE IMMEDIATE 'GRANT SELECT, INSERT ON TRANSACTION TO ' || p_username;
    EXECUTE IMMEDIATE 'GRANT SELECT ON USERS TO ' || p_username;
    EXECUTE IMMEDIATE 'GRANT EXECUTE ANY PROCEDURE TO ' || p_username;
    EXECUTE IMMEDIATE 'GRANT EXECUTE ANY FUNCTION TO ' || p_username;

    COMMIT;

    RETURN 1; -- Success
END;
-- tao fga policy de theo doi bang transaction
BEGIN
    DBMS_FGA.add_policy(
        object_schema   => 'KHANG3',
        object_name     => 'TRANSACTION',
        policy_name     => 'FGA_TRANSACTION',
        statement_types =>  'SELECT, INSERT, UPDATE, DELETE'
    );
END;

--- procedure de insert vao transaction 
CREATE OR REPLACE PROCEDURE insert_transaction (
    p_sender_user_name IN VARCHAR2,
    p_recipient_user_name IN VARCHAR2,
    p_transaction_type IN VARCHAR2,
    p_amount IN NUMBER
)
IS
    v_id NUMBER;
    v_userSenderId VARCHAR2(50);
    v_userRecipentId VARCHAR2(50);
BEGIN
    BEGIN
        SELECT transactionid
        INTO v_id
        FROM Khang3.transaction
        ORDER BY TRANSACTIONID
        FETCH FIRST 1 ROWS ONLY;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            -- No rows found, assign v_id to null
            v_id := 0;
    END;
    
    SELECT userId 
    into v_userSenderId
    from users
    where USERNAME=p_sender_user_name;
    
    SELECT userId 
    into v_userRecipentId
    from users
    where USERNAME=p_recipient_user_name;
        
    INSERT INTO Khang3.transaction (TRANSACTIONID, SENDERUSERID, RECIPIENTUSERID, TRANSACTIONTYPE, AMOUNT, TRANSACTIONDATE)
    VALUES (v_id + 1, v_userSenderId, v_userRecipentId, p_transaction_type, p_amount, CURRENT_TIMESTAMP);
    COMMIT; -- Commit the transaction
END;
------------------------------------delete
CREATE OR REPLACE PROCEDURE manage_datafile_or_tablespace(
    p_tablespace_name IN VARCHAR2,
    p_datafile_name IN VARCHAR2
) AS
  v_segment_count NUMBER;
  v_datafile_count NUMBER;
BEGIN
    -- Kiểm tra xem có đối tượng dữ liệu nào trong datafile không
    SELECT COUNT(*)
    INTO v_segment_count
    FROM dba_segments
    WHERE tablespace_name = p_tablespace_name
    AND (header_file = (SELECT file_id FROM dba_data_files WHERE file_name = p_datafile_name) OR
         header_file = (SELECT file_id FROM dba_data_files WHERE file_name = p_datafile_name));

    -- Kiểm tra xem tablespace có bao nhiêu datafile
    SELECT COUNT(*)
    INTO v_datafile_count
    FROM dba_data_files
    WHERE tablespace_name = p_tablespace_name;

    IF v_segment_count = 0 AND v_datafile_count = 1 THEN
        -- Nếu không có đối tượng dữ liệu và chỉ có một datafile, xóa tablespace
        EXECUTE IMMEDIATE 'DROP TABLESPACE ' || p_tablespace_name || ' INCLUDING CONTENTS AND DATAFILES CASCADE CONSTRAINTS';
    ELSIF v_segment_count = 0 AND v_datafile_count > 1 THEN
        -- Nếu không có đối tượng dữ liệu nhưng có nhiều datafile, chỉ xóa datafile
        EXECUTE IMMEDIATE 'ALTER DATABASE DATAFILE ''' || p_datafile_name || ''' OFFLINE DROP';
    ELSE
        -- Nếu datafile chứa dữ liệu, không thực hiện thao tác
        RAISE_APPLICATION_ERROR(-20000, 'Datafile contains data and cannot be dropped.');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        -- Xử lý lỗi
        RAISE;
END manage_datafile_or_tablespace;
--------new
CREATE OR REPLACE FUNCTION get_audit_policies RETURN SYS_REFCURSOR IS
    audit_cursor SYS_REFCURSOR;
BEGIN
    -- Open a cursor for the query
    OPEN audit_cursor FOR
        SELECT OBJECT_SCHEMA,
                OBJECT_NAME,
                POLICY_OWNER,
                POLICY_NAME,
                ENABLED
                FROM DBA_AUDIT_POLICIES;

    -- Return the cursor
    RETURN audit_cursor;
END;
/

desc DBA_AUDIT_POLICIES;

-----Hieu-----
CREATE OR REPLACE FUNCTION get_list_user
RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT USER_ID, USERNAME, ACCOUNT_STATUS FROM DBA_USERS;
    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION get_info_user_by_id(
    id IN VARCHAR2
)
RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT USER_ID, USERNAME, CREATED, EXPIRY_DATE, ACCOUNT_STATUS, LAST_LOGIN, PROFILE 
    FROM DBA_USERS
    WHERE USER_ID = id;

    RETURN v_cursor;
END;

------kiet

CREATE OR REPLACE PROCEDURE Change_TBSpace_Unlock(
    p_user_name IN VARCHAR2,
    p_new_tablespace IN VARCHAR2
) AS
BEGIN
    EXECUTE IMMEDIATE 'ALTER USER ' || p_user_name || ' DEFAULT TABLESPACE ' || p_new_tablespace;
    EXECUTE IMMEDIATE 'ALTER USER ' || p_user_name || ' ACCOUNT UNLOCK';
    DBMS_OUTPUT.PUT_LINE('User ' || p_user_name || ' has been modified and unlocked successfully.');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END Change_TBSpace_Unlock;

---Hieu---
create or replace FUNCTION ADD_FGA_POLICY (
    p_object_schema IN VARCHAR2,
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2,
    p_type IN VARCHAR2
) RETURN VARCHAR2 AS
BEGIN
    DBMS_FGA.add_policy(
        object_schema   => p_object_schema,
        object_name     => p_object_name,
        policy_name     => p_policy_name,
        statement_types => p_type
    );

    RETURN 'Policy added successfully';
END ADD_FGA_POLICY;

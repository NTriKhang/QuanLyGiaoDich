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

create or replace PROCEDURE Change_TBSpace_Unlock(
    p_user_name IN VARCHAR2,
    p_new_tablespace IN VARCHAR2,
    p_quota IN NUMBER
) AS
BEGIN
    EXECUTE IMMEDIATE 'ALTER USER ' || p_user_name || ' QUOTA ' || TO_CHAR(p_quota) || 'M ON ' || p_new_tablespace;
    EXECUTE IMMEDIATE 'ALTER USER ' || p_user_name || ' DEFAULT TABLESPACE ' || p_new_tablespace;
    EXECUTE IMMEDIATE 'ALTER USER ' || p_user_name || ' ACCOUNT UNLOCK';
END;

-----set quota

CREATE OR REPLACE PROCEDURE get_tablespace_size(
    p_tablespace_name IN VARCHAR2,
    p_tablespace_size OUT NUMBER
) AS
BEGIN
    SELECT SUM(bytes)/1024/1024 INTO p_tablespace_size 
    FROM dba_data_files
    WHERE tablespace_name = p_tablespace_name;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_tablespace_size := -1; 
    WHEN OTHERS THEN
        p_tablespace_size := -1; 
END;
-------thiet lap canh bao 
BEGIN
    DBMS_FGA.ADD_POLICY(
        object_schema   => 'KHANG3',
        object_name     => 'TRANSACTION',
        policy_name     => 'HIGH_VALUE_TRANSFER_AUDIT',
        audit_condition => 'AMOUNT > 100000000',
        audit_column    => 'AMOUNT',
        handler_schema  => 'KHANG3',
        handler_module  => 'ALERT_HIGH_VALUE_TRANSFER',
        statement_types => 'INSERT, SELECT',
        enable          => TRUE
    );
END;
/

CREATE TABLE Alert (
    AlertID NUMBER PRIMARY KEY,
    Message VARCHAR2(255),
    CreatedDate TIMESTAMP,
    UserName VARCHAR2(50)
);
CREATE SEQUENCE ALERT_SEQ
START WITH 1
INCREMENT BY 1
NOCACHE
NOCYCLE;


CREATE OR REPLACE PROCEDURE alert_high_value_transfer (
    object_schema  VARCHAR2,
    object_name    VARCHAR2,
    policy_name    VARCHAR2
)
IS
    v_user_name VARCHAR2(50);
BEGIN
    v_user_name := SYS_CONTEXT('USERENV', 'SESSION_USER');

    INSERT INTO Alert (AlertID, Message, CreatedDate, UserName)
    VALUES (ALERT_SEQ.NEXTVAL, 'C?nh báo: Giao d?ch v??t quá 100 tri?u', SYSDATE, v_user_name);
    
    COMMIT;
END;
/

---Hieu---
create or replace FUNCTION ADD_FGA_POLICY (
    p_object_schema IN VARCHAR2,
    p_object_name IN VARCHAR2,npo
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

create or replace FUNCTION GET_TABLES_BY_OWNER(
    p_owner IN VARCHAR2
) RETURN SYS_REFCURSOR IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
        SELECT table_name 
        FROM all_tables 
        WHERE owner = UPPER(p_owner);

    RETURN v_cursor;
END GET_TABLES_BY_OWNER;

------------------------V4
---------Khang
CREATE OR REPLACE FUNCTION get_table_columns(table_name IN VARCHAR2)
RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
    v_column_name varchar2(128);
    v_table varchar2(128);
BEGIN
    v_table := upper(table_name);
    OPEN v_cursor FOR
        SELECT COLUMN_NAME
        FROM ALL_TAB_COLUMNS
        WHERE TABLE_NAME = v_table;
    RETURN v_cursor;
END get_table_columns;
/
-----------------------
CREATE OR REPLACE PROCEDURE create_group_privilege(
    p_role_name IN VARCHAR2,
    p_execute_cmds IN VARCHAR2, 
    p_table_names IN VARCHAR2 
)
IS
    role_exists INTEGER;
BEGIN
    -- Check if the role already exists
    SELECT COUNT(*)
    INTO role_exists
    FROM dba_roles
    WHERE role = upper(p_role_name);

    IF role_exists = 0 THEN
        EXECUTE IMMEDIATE 'CREATE ROLE ' || upper(p_role_name);
    END IF;

    EXECUTE IMMEDIATE 'GRANT ' || p_execute_cmds || ' ON ' || p_table_names || ' TO ' || upper(p_role_name);
END create_group_privilege;
/

/
desc dba_roles
/
BEGIN
    create_group_privilege('your_role_name', 'Delete', 'BENEFICIARY');
END;
select * from dba_roles;

CREATE OR REPLACE FUNCTION role_exists(p_role_name IN VARCHAR2)
RETURN INTEGER
IS
    role_count INTEGER;
BEGIN
    -- Check if the role exists
    SELECT COUNT(*)
    INTO role_count
    FROM dba_roles
    WHERE role = upper(p_role_name);

    -- Return true if role exists, false otherwise
    IF role_count > 0 then
        return 1;
    else
        return 0;
    end if;
END role_exists;
/
CREATE OR REPLACE FUNCTION get_all_roles RETURN SYS_REFCURSOR
IS
    cur SYS_REFCURSOR;
BEGIN
    OPEN cur FOR
        SELECT role, oracle_maintained
        FROM dba_roles;

    RETURN cur;
END get_all_roles;
/
CREATE OR REPLACE FUNCTION get_role_privileges(role_name IN VARCHAR2)
  RETURN SYS_REFCURSOR
IS
  cur SYS_REFCURSOR;
BEGIN
  OPEN cur FOR
    SELECT TABLE_NAME, PRIVILEGE
    FROM DBA_TAB_PRIVS
    WHERE GRANTEE = upper(role_name);
  RETURN cur;
END;
/
CREATE OR REPLACE FUNCTION get_users_assigned_to_role(role_name IN VARCHAR2)
  RETURN SYS_REFCURSOR
IS
  cur SYS_REFCURSOR;
BEGIN
  OPEN cur FOR
    SELECT GRANTEE
    FROM DBA_ROLE_PRIVS
    WHERE GRANTED_ROLE = upper(role_name);
  RETURN cur;
END;
/
CREATE OR REPLACE PROCEDURE assign_role_to_user(
    p_role_name IN VARCHAR2,
    p_username  IN VARCHAR2
)
IS
BEGIN
    EXECUTE IMMEDIATE 'GRANT ' || upper(p_role_name) || ' TO ' || upper(p_username);
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END assign_role_to_user;
/
----Hieu----
create or replace FUNCTION get_audit_trial
RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT
        SESSION_ID,
        TO_CHAR(TIMESTAMP, 'DD-MM-YYYY HH24:MI:SS') AS TIMESTAMP,
        DB_USER,
        OBJECT_SCHEMA,
        OBJECT_NAME,
        SQL_TEXT
    FROM dba_fga_audit_trail;
    RETURN v_cursor;
END;

CREATE OR REPLACE FUNCTION get_profiles
RETURN SYS_REFCURSOR
IS
v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT PROFILE, RESOURCE_NAME, LIMIT
    FROM dba_profiles;
    RETURN v_cursor;
END;

create or replace FUNCTION add_profile (
    p_profile_name IN VARCHAR2,
    p_session_per_user IN NUMBER,
    p_idle_time IN NUMBER,
    p_connect_time IN NUMBER
) RETURN NUMBER
IS
    profile_count NUMBER;
BEGIN
    SELECT COUNT(*)
    INTO profile_count
    FROM dba_profiles
    WHERE UPPER(PROFILE) = p_profile_name;
    
    IF profile_count > 0 THEN
        RETURN 0;
    ELSE
        EXECUTE IMMEDIATE 'CREATE PROFILE ' || p_profile_name ||
        ' LIMIT SESSIONS_PER_USER ' || p_session_per_user || 
        ' IDLE_TIME ' || p_idle_time ||
        ' CONNECT_TIME ' || p_connect_time;
        RETURN 1;
    END IF;
END;

create or replace FUNCTION assign_profile(
    p_profile VARCHAR2,
    P_user_name VARCHAR2
)
RETURN NUMBER
IS
BEGIN
    EXECUTE IMMEDIATE 'ALTER USER ' || P_user_name || 
    ' PROFILE ' || p_profile;
    RETURN 1;
END;

CREATE OR REPLACE FUNCTION grant_privilege(
    p_username VARCHAR2,
    p_table VARCHAR2,
    p_privilege VARCHAR2
) RETURN NUMBER AS
BEGIN
    EXECUTE IMMEDIATE 'GRANT ' || p_privilege || 
    ' ON ' || p_table || ' TO ' || p_username;
    RETURN 1;
END;

create or replace FUNCTION revoke_privilege(
    p_username VARCHAR2,
    p_table VARCHAR2,
    p_privilege VARCHAR2
) RETURN NUMBER AS
BEGIN
    EXECUTE IMMEDIATE 'REVOKE ' || p_privilege || 
    ' ON ' || p_table || ' FROM ' || p_username;   
    RETURN 1;
END;

create or replace FUNCTION get_privilege_user(
    p_name VARCHAR2
)
RETURN SYS_REFCURSOR
IS
    v_cursor SYS_REFCURSOR;
BEGIN
    OPEN v_cursor FOR
    SELECT TABLE_NAME, PRIVILEGE FROM DBA_TAB_PRIVS WHERE GRANTEE = p_name;
    RETURN v_cursor;
END;


----kiet V4--------
ALTER TABLE Alert ADD IsProcessed NUMBER(1) DEFAULT 0 NOT NULL;

CREATE OR REPLACE PROCEDURE Get_User_Transactions(
    p_UserName IN VARCHAR2,
    o_Transactions OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN o_Transactions FOR
        SELECT 
            t.TransactionID,
            u1.UserName AS UserNameGui,
            u2.UserName AS UserNameNhan,
            t.TransactionType,
            t.Amount,
            t.TransactionDate
        FROM Transaction t
        JOIN Users u1 ON t.SenderUserID = u1.UserID
        JOIN Users u2 ON t.RecipientUserID = u2.UserID
        WHERE u1.UserName = p_UserName OR u2.UserName = p_UserName;
        ORDER BY t.TransactionDate DESC;
END;

create or replace PROCEDURE Get_User_Alerts (
    p_username IN VARCHAR2,
    out_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN out_cursor FOR
        SELECT a.Message,
               a.CreatedDate
        FROM Alert a
        WHERE a.UserName = p_username
        ORDER BY a.CreatedDate DESC;
END;

---v5------
---Hieu---
create or replace FUNCTION ADD_FGA_POLICY (
    p_object_schema IN VARCHAR2,
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2,
    p_type IN VARCHAR2,
    p_audit_condition IN VARCHAR2
) RETURN VARCHAR2 AS
    v_audit_condition VARCHAR2(1000);
BEGIN
    IF p_audit_condition = 'All' THEN
        v_audit_condition := '1=1';
    ELSE
        v_audit_condition := 'SYS_CONTEXT(''USERENV'', ''SESSION_USER'') = ''' || UPPER(p_audit_condition) || '''';
    END IF;

    DBMS_FGA.add_policy(
        object_schema   => p_object_schema,
        object_name     => p_object_name,
        policy_name     => p_policy_name,
        audit_condition => v_audit_condition,
        statement_types => p_type
    );

    RETURN 'Policy added successfully';
END ADD_FGA_POLICY;

create or replace FUNCTION DELETE_FGA_POLICY (
    p_object_schema IN VARCHAR2,
    p_object_name IN VARCHAR2,
    p_policy_name IN VARCHAR2
) RETURN VARCHAR2 AS
BEGIN
        DBMS_FGA.DROP_POLICY(
        OBJECT_NAME => p_object_name,
        OBJECT_SCHEMA => p_object_schema,
        POLICY_NAME => p_policy_name
    );

    RETURN 'Policy deleted successfully';
END DELETE_FGA_POLICY;
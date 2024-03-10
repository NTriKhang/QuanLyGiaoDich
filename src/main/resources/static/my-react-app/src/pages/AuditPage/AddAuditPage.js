import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";

const AddAuditPage = (props) => {
    const dataFetchedRef = React.useRef(false);
    const [tableName, setTableName] = useState([]);
    const [objectName, setObjectName] = useState([]);
    const [policyName, setPolicyName] = useState([]);
    const [statementType, setStatementType] = useState("");
    const [selectCheck, setSelectCheck] = useState(false);
    const [updateCheck, setUpdateCheck] =useState(false);
    const [deleteCheck, setDeleteCheck] = useState(false);
    const [insertCheck, setInsertCheck] = useState(false);
    const navigate = useNavigate();
    const getTablename = () => {
        fetch('http://localhost:8080/api/v1/audit/getAllTable', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setTableName(data);
        });
    }
    
    const addAudit = (objectName, policyName, statementType) => {
        try {
            const audit = {
                p_object_name: objectName,
                p_policy_name: policyName,
                p_type: statementType
            }

            fetch('http://localhost:8080/api/v1/audit/addAudit', {
            method: 'POST',
            body: JSON.stringify(audit),
        })
        .then(res => {
            if(!res.ok) {
                throw new Error("Something went wrong!");
            }
            return res.json();
        })
        .then(res => {
            navigate("/auditManage");
        })
        }
        catch(err) {
            console.log("Error when fetching: " + err);
        }
    }

    const changeStatementType = () => {
        var result = [];
       if(selectCheck) {
        result.push("SELECT");
       }
       if (insertCheck) {
        result.push("INSERT");
       }
       if (deleteCheck) {
        result.push("DELETE");
       }
       if (updateCheck) {
        result.push("UPDATE");
       }

       var listStatement = "";
       result.forEach((item, index) => {
        if(index == 0) {
            listStatement += item;
        }
        else {
            listStatement = (listStatement + ", " + item);
        }
       });
       setStatementType(listStatement);
    }

    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        getTablename();
    }, []);

    useEffect(() => {
        changeStatementType();
    }, [selectCheck, insertCheck, updateCheck, deleteCheck])
    
    return (
        <div>
          <Navbar />
          <div className="container w-25 p-0">
            <h2>Add Audit</h2>
            <div>
                <div className="form-group mt-4">
                    <select
                        id="objectName"
                        value={objectName}
                        onChange={e => setObjectName(e.target.value)}
                    >
                        <option value={null}>Choose Object Name...</option>
                        {Array.isArray(tableName) && tableName.map(table => (
                            <option key={table} value={table}>{table}</option>
                        ))}
                    </select>
                </div>
            </div>
            <div>
                <input
                    value={policyName}
                    placeholder="Policy Name"
                    onChange={ev => setPolicyName(ev.target.value)}
                    className={"inputBox form-control pt-4 pb-4"}
                    type="text" />
            </div>
            <div>
            <div className="form-check" style={{flexDirection: 'row', display: 'flex', marginTop: '18px'}}>
                <div className="w-10">
                    <input
                        value={selectCheck}
                        onChange={ev => {
                            selectCheck ? setSelectCheck(false) : setSelectCheck(true);
                        }}
                        className={"form-check-input"}
                        type="checkbox" />
                    <label className="form-check-label" for="flexCheckDefault">
                        <p style={{fontWeight: 'bold'}}>SELECT</p>
                    </label>
                </div>
                <div className="w-10 mx-5">
                    <input
                        value={insertCheck}
                        onChange={ev => {
                            insertCheck ? setInsertCheck(false) : setInsertCheck(true);
                        }}
                        className={"form-check-input"}
                        type="checkbox" />
                    <label className="form-check-label" for="flexCheckDefault">
                        <p style={{fontWeight: 'bold'}}>INSERT</p>
                    </label>
                </div>
                <div className="w-10">
                    <input
                        value={deleteCheck}
                        onChange={ev => {
                            deleteCheck ? setDeleteCheck(false) : setDeleteCheck(true);
                        }}
                        className={"form-check-input"}
                        type="checkbox" />
                    <label className="form-check-label" for="flexCheckDefault">
                        <p style={{fontWeight: 'bold'}}>DELETE</p>
                    </label>
                </div>
                <div className="w-10 mx-5">
                    <input
                        value={updateCheck}
                        onChange={ev => {
                            updateCheck ? setUpdateCheck(false) : setUpdateCheck(true);
                        }}
                        className={"form-check-input"}
                        type="checkbox" />
                    <label className="form-check-label" for="flexCheckDefault">
                        <p style={{fontWeight: 'bold'}}>UPDATE</p>
                    </label>
                </div>
            </div>
            </div>
          <div>
            <button 
                className="btn btn-primary w-100"
                onClick={() => {
                    addAudit(objectName, policyName, statementType);
                }}    >
                    Save
            </button>
          </div>
          </div>
        </div>  
      );
}


export default AddAuditPage;
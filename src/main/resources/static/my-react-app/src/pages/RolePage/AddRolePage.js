import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";

const AddRolePage = (props) => {
    const dataFetchedRef = React.useRef(false);
    const [hiddenState, setHiddenState] = useState({});
    const [checkboxValues, setCheckboxValues] = useState({}); // State to manage checkbox values
    const [tableName, setTableName] = useState([]);
    const [roleName, setRoleName] = useState();
    const navigate = useNavigate();

    const toggleTableCheckbox = (tableName) => {
        setCheckboxValues(prevState => ({
            ...prevState,
            [tableName]: prevState[tableName] ? {} : {
                select: false,
                create: false,
                update: false,
                delete: false
            }
        }));
    };
    const toggleCheckbox = (tableName, privilege) => {
        setCheckboxValues(prevState => ({
            ...prevState,
            [tableName]: {
                ...prevState[tableName],
                [privilege]: !prevState[tableName]?.[privilege] || false
            }
        }));
    };

    const handleSubmit = () => {
        // Logic to upload checkbox values to your database
        let roleList = [];
        for (const tableName in checkboxValues) {
            if (checkboxValues.hasOwnProperty(tableName)) {
                const tableValues = checkboxValues[tableName];
                if (Object.values(tableValues).some(value => value === true)) {
                    let execute = "";
                    for (const statement in tableValues) {
                        console.log(tableValues[statement])
                        if (tableValues[statement] === true) {
                            execute += statement + ',';
                        }
                    }
                    console.log(execute.length, execute)
                    execute = execute.slice(0, -1);
                    let role = {
                        tableName: tableName,
                        roleName: roleName,
                        executeCmd: execute
                    }
                    console.log(role);
                    roleList.push(role)
                }
                // Here you can send tableValues to your backend for storage
            }
        }
        console.log(roleList)
        try {
            fetch('http://localhost:8080/api/v1/privilege', {
                method: 'POST',
                // mode: 'no-cors',
                // headers:{
                //     "Content-type": "multipart/form-data",
                // },
                body: JSON.stringify(roleList)
            }).then(res => {
                console.log(res)
                if(res.status == 409){
                    alert("role already exist")
                }
                else if(res.status == 400){
                    alert("invalid parameter")
                }
                else{
                    alert("create success")
                    navigate('/roleManage')
                }
            });
        } catch {
            console.log("err")
        }

    };

    const toggleHidden = (index, table) => {
        setHiddenState((prevState) => ({
            ...prevState,
            [index]: !prevState[index] // Toggle hidden state for the clicked index
        }));
        toggleTableCheckbox(table)
    };

    const getTablename = () => {
        fetch('http://localhost:8080/api/v1/audit/getAllTable', {
            method: 'GET'
        })
            .then(response => response.json())
            .then(data => {
                setTableName(data);
            });
    }

    // const addAudit = (objectName, policyName, statementType) => {
    //     try {
    //         const audit = {
    //             p_object_name: objectName,
    //             p_policy_name: policyName,
    //             p_type: statementType
    //         }

    //         fetch('http://localhost:8080/api/v1/audit/addAudit', {
    //             method: 'POST',
    //             body: JSON.stringify(audit),
    //         })
    //             .then(res => {
    //                 if (!res.ok) {
    //                     throw new Error("Something went wrong!");
    //                 }
    //                 return res.json();
    //             })
    //             .then(res => {
    //                 navigate("/auditManage");
    //             })
    //     }
    //     catch (err) {
    //         console.log("Error when fetching: " + err);
    //     }
    // }

    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        getTablename();
    }, []);

    return (
        <div>
            <Navbar />
            <div className="container w-25 p-0">
                <h2>Add Role</h2>
                <div>
                    <input
                        value={roleName}
                        placeholder="Role Name"
                        onChange={ev => setRoleName(ev.target.value)}
                        className={"inputBox form-control pt-4 pb-4"}
                        type="text" />
                </div>

                <div className="">
                    <div className="row mb-3">
                        <div className="col-4">
                            <p>TABLE</p>
                        </div>
                        <div className="col-8">
                            <p>STATEMENT</p>
                        </div>
                    </div>
                    <div className="">
                        {tableName.map((table, index) => (
                            <div key={index} className="row" >
                                <div className="col-5 mb-3">
                                    {table} <input type="checkbox" onChange={() => toggleHidden(index, table)} />
                                </div>
                                <div className="col-7 mb-3" style={{ display: hiddenState[index] ? 'block' : 'none' }}>
                                    <div className="row" >
                                        <div className="col-3">
                                            <input type="checkbox" checked={checkboxValues[table]?.select} onChange={() => toggleCheckbox(table, 'select')} />
                                            <span htmlFor="flexCheckDefault">
                                                SELECT
                                            </span>
                                        </div>
                                        <div className="col-3">
                                            <input type="checkbox" checked={checkboxValues[table]?.insert} onChange={() => toggleCheckbox(table, 'insert')} />
                                            <span htmlFor="flexCheckDefault">
                                                INSERT
                                            </span>
                                        </div>
                                        <div className="col-3">
                                            <input type="checkbox" checked={checkboxValues[table]?.update} onChange={() => toggleCheckbox(table, 'update')} />
                                            <span htmlFor="flexCheckDefault">
                                                UPDATE
                                            </span>
                                        </div>
                                        <div className="col-3">
                                            <input type="checkbox" checked={checkboxValues[table]?.delete} onChange={() => toggleCheckbox(table, 'delete')} />
                                            <span htmlFor="flexCheckDefault">
                                                DELETE
                                            </span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                </div>
                <div>
                    <button onClick={handleSubmit}>Submit</button>
                </div>
            </div>
        </div>
    );
}


export default AddRolePage;
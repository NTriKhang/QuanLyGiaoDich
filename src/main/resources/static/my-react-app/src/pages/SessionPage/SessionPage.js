import React, { useState } from "react";
import List from "../../components/listInfo/ListInfo";
import Navbar from "../../components/navbar/Navbar";

const SessionPage = (props) => {
    const [isOpen, setIsOpen] = useState(true);
    const [sessionInfo, setSessionInfo] = useState([]);
    const [processInfo, setProcessInfo] = useState({
        "OS_Process_ID": 16580,
		"address": "AAB/+/dtRKg=",
		"PGA_USED_MEM": 1007767,
		"PGA_ALLOC_MEM": 1217087,
		"PGA_FREEABLE_MEM": 0,
		"PGA_MAX_MEM": 1217087
    });
    const dataFetchedRef = React.useRef(false);
    const openDialog = (sid) => {
        fetch('http://localhost:8080/api/v1/session/info/' + sid, {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data[0])
            setProcessInfo(data[0]);
        });
        document.getElementById('process_dialog').showModal();
      };
    
      const closeDialog = () => {
        document.getElementById('process_dialog').close();
      };
    const showSession = () => {
        fetch('http://localhost:8080/api/v1/session/info', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data[0])
            setSessionInfo(data[0]);
        });
    }
    const kill_session = (sid_p, serial_p, index) => {
        try{
            var sessionInfo = {
                sid: sid_p,
                serial: serial_p
            }
            console.log(sessionInfo)
            fetch('http://localhost:8080/api/v1/session/kill_session',{
                method: 'POST',
                // mode: 'no-cors',
                // headers:{
                //     "Content-type": "multipart/form-data",
                // },
     
                body: JSON.stringify(sessionInfo)
            }).then(res => {
                console.log(res)
                if(res.status == 200){
                    setSessionInfo(prevSessionInfo => {
                        const newSessionInfo = [...prevSessionInfo];
                        newSessionInfo.splice(index, 1);
                        return newSessionInfo;
                    });
                }

            });
        }catch{
            console.log("err")
        }
    }
    React.useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showSession();
    }, [])
    return(
        <div>
            <Navbar></Navbar>
            <table>
                <thead>
                    <tr>
                        <th>SID</th>
                        <th>Serial</th>
                        <th>User name</th>
                        <th>Program Name</th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {sessionInfo.map((session, index) => (
                        <tr key={index}>
                            <td>{session.SID}</td>
                            <td>{session['SERIAL#']}</td>
                            <td>{session.USERNAME}</td>
                            <td>{session.PROGRAM}</td>
                            <td>
                                <button className="btn btn-danger" onClick={() => kill_session(session.SID, session['SERIAL#'], index)}>Kill</button>
                            </td>
                            <td>
                                <button className="btn btn-primary" onClick={() => openDialog(session.SID)}>Process</button>

                            </td>
                            
                        </tr>
                    ))}
                </tbody>
            </table>
            {isOpen && (
                <dialog  id="process_dialog">
                    <table>
                        <thead>
                            <tr>
                                <th>OS_Process_ID</th>
                                <th>address</th>
                                <th>PGA_USED_MEM</th>
                                <th>PGA_ALLOC_MEM</th>
                                <th>PGA_FREEABLE_MEM</th>
                                <th>PGA_MAX_MEM</th>
                            </tr>
                        </thead>
                        <tbody>
                            {[processInfo].map((process, index) => (
                                <tr key={index}>
                                    <td>{process.OS_Process_ID}</td>
                                    <td>{process['address#']}</td>
                                    <td>{process.PGA_USED_MEM}</td>
                                    <td>{process.PGA_ALLOC_MEM}</td>
                                    <td>{process.PGA_FREEABLE_MEM}</td>
                                    <td>{process.PGA_MAX_MEM}</td>
                                    
                                </tr>
                            ))}
                        </tbody>
                    </table>
                    <button onClick={closeDialog}>Close</button>
                </dialog>
            )}
        </div>
        
    )
};

export default SessionPage;
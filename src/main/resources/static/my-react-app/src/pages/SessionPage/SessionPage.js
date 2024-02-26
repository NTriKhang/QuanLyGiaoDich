import React, { useState } from "react";
import List from "../../components/listInfo/ListInfo";
import Navbar from "../../components/navbar/Navbar";

const SessionPage = (props) => {
    const [sessionInfo, setSessionInfo] = useState([]);
    const dataFetchedRef = React.useRef(false);

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
                            <td><button className="btn btn-primary">Process</button></td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    )
};

export default SessionPage;
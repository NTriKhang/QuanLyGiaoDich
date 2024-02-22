import React, { useState } from "react";
import { json, useNavigate } from "react-router-dom";

import Navbar from "../../components/navbar/Navbar";
import Button from "react-bootstrap/esm/Button";
import List from "../../components/listInfo/ListInfo";

const ManageDBPage = () => {
    const [activeComponent, setActiveComponent] = useState(null);
    const [listInfo, setListInfo] = useState([]);

    const headersSga = ['CON_ID', 'VALUE', 'NAME'];
    const headersPga = ['UNIT', 'CON_ID', 'VALUE', 'NAME'];
    const headersProcess = ['PID', 'SPID', 'PROGRAM'];
    const headersDatafile = ['BYTES', 'STATUS', 'TABLESPACE_NAME', 'FILE_ID', 'FILE_NAME'];
    const headersInstance = ['STARTUP_TIME', 'INSTANCE_NUMBER', 'INSTANCE_NAME', 'STATUS', 'VERSION', 'DATABASE_STATUS'];
    const headerControlfile = ['STATUS', 'NAME'];
    const headerSpfile = ['STATUS', 'NAME'];

    const showSga = () => {
        setActiveComponent("Sga");
        fetch('http://localhost:8080/api/v1/inforDB/sga', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showPga = () => {
        setActiveComponent("Pga");
        fetch('http://localhost:8080/api/v1/inforDB/pga', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showProcess = () => {
        setActiveComponent("Process");
        fetch('http://localhost:8080/api/v1/inforDB/process', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showDatafile = () => {
        setActiveComponent("Datafile");
        fetch('http://localhost:8080/api/v1/inforDB/datafile', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showInstance = () => {
        setActiveComponent("Instance");
        fetch('http://localhost:8080/api/v1/inforDB/instance', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showControlfile = () => {
        setActiveComponent("Controlfile");
        fetch('http://localhost:8080/api/v1/inforDB/controlfile', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    const showSpfile = () => {
        setActiveComponent("Spfile");
        fetch('http://localhost:8080/api/v1/inforDB/spfile', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setListInfo(data[0]);
        });
    }

    return (
        <div>
            <div>
                <Navbar />
                <h2 className="m-4">Manage Database Page</h2>
                <Button 
                    className="me-2"
                    onClick={() => showSga()} >
                    Sga
                </Button>
                <Button 
                    className="me-2"
                    onClick={() => showPga()} >
                    Pga
                </Button>
                <Button 
                    className="me-2"
                    onClick={() => showProcess()} >
                    Process
                </Button>
                <Button 
                    className="me-2"
                    onClick={() => showInstance()} >
                    Instance
                </Button>
                <Button 
                    className="me-2"
                    onClick={() => showDatafile()} >
                    Datafile
                </Button>
                <Button 
                    className="me-2"
                    onClick={() => showControlfile()} >
                    Control Files
                </Button>
                <Button
                    onClick={() => showSpfile()} >
                    Spfile
                </Button>
            </div>
            <div className="container mt-5 table-bordered">
                {activeComponent === 'Sga' && <List listInfo={listInfo} headers={headersSga} />}
                {activeComponent === 'Pga' && <List listInfo={listInfo} headers={headersPga} />}
                {activeComponent === 'Process' && <List listInfo={listInfo} headers={headersProcess} />}
                {activeComponent === 'Datafile' && <List listInfo={listInfo} headers={headersDatafile} />}
                {activeComponent === 'Instance' && <List listInfo={listInfo} headers={headersInstance} />}
                {activeComponent === 'Controlfile' && <List listInfo={listInfo} headers={headerControlfile} />}
                {activeComponent === 'Spfile' && <List listInfo={listInfo} headers={headerSpfile} />}
            </div>
        </div>
    ) 
};

export default ManageDBPage;
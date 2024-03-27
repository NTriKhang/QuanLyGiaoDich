import React, {useState, useEffect} from "react";
import { useNavigate } from "react-router-dom";

import Navbar from "../../components/navbar/Navbar";

const AuditTrial = () => {
    const dataFetchedRef = React.useRef(false);
    const [listInfo, setListInfo] = useState([]);

    const getAuditTrailTable = () => {
        fetch("http://localhost:8080/api/v1/audit/getAuditTrail", {
            method: "GET"
        })
        .then(response => response.json()) 
        .then(res => {
            console.log(res)
            setListInfo(res);
        })
    }

    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        getAuditTrailTable();
    }, [])
    
    return (
        <div>
            <Navbar />
            <div style={{marginLeft: "100px", marginRight: "100px"}}>
                <table className="table table-hover">
                  <thead className="table-active">
                      <tr>
                          <th className="p-3">SESSION_ID</th>
                          <th className="p-3">TIMESTAMP</th>
                          <th className="p-3">DB_USER</th>
                          <th className="p-3">OBJECT_SCHEMA</th>
                          <th className="p-3">OBJECT_NAME</th>
                          <th className="p-3">SQL_TEXT</th>
                      </tr>
                  </thead>
                  <tbody>
                      {listInfo?.map((data, index) => (
                          <tr key={index}>
                              <td className="text-center">{data.SESSION_ID}</td>
                              <td className="text-center">{data.TIMESTAMP}</td>
                              <td className="text-center">{data.DB_USER}</td>
                              <td className="text-center">{data.OBJECT_SCHEMA}</td>
                              <td className="text-center">{data.OBJECT_NAME}</td>
                              <td className="text-center">{data.SQL_TEXT}</td>
                          </tr>
                      ))}
                  </tbody>
              </table>
            </div>
        </div>
    );
};

export default AuditTrial;
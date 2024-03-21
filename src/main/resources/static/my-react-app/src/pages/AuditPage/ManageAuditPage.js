import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../../components/navbar/Navbar";

const ManageAuditPage = (props) => {
    const navigate = useNavigate();
    const dataFetchedRef = React.useRef(false);
    const [listInfo, setListInfo] = useState([]);

    const showListPolicy = () => {
        fetch('http://localhost:8080/api/v1/audit', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            console.log(data[0])
            setListInfo(data);
        });
    }
    
    useEffect(() => {
        if (dataFetchedRef.current) return;
        dataFetchedRef.current = true;

        showListPolicy();
        console.log(listInfo)
    }, []);
    return (
        <div>
          <Navbar />
          <div className="container">
          <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/addAudit")
            }} >Create new</button>
             <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/auditManage")
            }} >Show audit</button>
            
             <button 
            className="btn btn-primary me-2"
            onClick={() => {
                navigate("/auditWarning")
            }} >Show warning</button>
            
            
          <table>
                  <thead>
                      <tr>
                          <th>OBJECT_SCHEMA</th>
                          <th>OBJECT_NAME</th>
                          <th>POLICY_OWNER</th>
                          <th>POLICY_NAME</th>
                          <th>ENABLED</th>
                      </tr>
                  </thead>
                  <tbody>
                      {listInfo.map((policy, index) => (
                          <tr key={index}>
                              <td className="text-center">{policy.OBJECT_SCHEMA}</td>
                              <td className="text-center">{policy.OBJECT_NAME}</td>
                              <td className="text-center">{policy.POLICY_OWNER}</td>
                              <td className="text-center">{policy.POLICY_NAME}</td>
                              <td className="text-center">{policy.ENABLED}</td>
                          </tr>
                      ))}
                  </tbody>
              </table>
          </div>
        </div>  
      );
}


export default ManageAuditPage;
import React, { useEffect, useState } from "react";

const useGetTableName = () => {
    const [tableName, setTableName] = useState([]);

	const fetchTableData = async () => {
        await fetch('http://localhost:8080/api/v1/audit/getAllTable', {
            method: 'GET'
        })
        .then(response => response.json())
        .then(data => {
            setTableName(data);
        });
    }

    useEffect(() => {
        fetchTableData();
    }, [])
    return [tableName]
};

export default useGetTableName;
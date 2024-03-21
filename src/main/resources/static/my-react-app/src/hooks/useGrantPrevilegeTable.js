import React, { useEffect } from "react";

const useGrantPrevilegeTable = (username, table, privilege) => {
    const fetchPrivilege = () => {
		try {
			const grantPrivilege = {
				p_username: username,
				p_table: table,
                p_privilege:privilege
			}

			fetch('http://localhost:8080/api/v1/privilege/grantTable', {
				method: 'POST',
				body: JSON.stringify(grantPrivilege)
			})
			.then(response => {
				if(!response.ok) {
					console.log("Error when fetching !!!");
				}
				return response.json();
			})
			.then(response => {
				console.log(response);
				console.log("Successfully !!!");
			})
		}
		catch(err) {
			console.log(err);
		}
	}

    useEffect(() => {
        fetchPrivilege();
    }, [username, table, privilege])
};

export default useGrantPrevilegeTable;
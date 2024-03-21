import { Button, Select, Tag, Flex } from "antd";
import React, { useEffect, useState } from "react";

const tagRender = (props) => {
	const { label, value, closable, onClose } = props;
	const onPreventMouseDown = (event) => {
		event.preventDefault();
		event.stopPropagation();
	};
	return (
		<Tag
			color={'cyan'}
			onMouseDown={onPreventMouseDown}
			closable={closable}
			onClose={onClose}
			style={{
				marginInlineEnd: 4,
			}}
			>
			{label}
		</Tag>
	);
	};
const RevokeForm = (props) => {
    const [listPrivilege, setListPrivilege] = useState([]);
    const [optionTable, setOptionTable] = useState([]);
    const [listPrivilegeRevoke, setListPrivilegeRevoke] = useState([]);

    const getListPrivilegeOfUser = (name) => {
        try {
			fetch(`http://localhost:8080/api/v1/privilege/getUserPrivilege/${name}`, {
				method: 'GET',
			})
			.then(response => {
				if(!response.ok) {
					console.log("Error when fetching !!!");
					return;
				}
				return response.json();
			})
            .then(response => {
                setListPrivilege(response);
            })
		}
		catch(err) {
			console.log(err);
		}
    }

    const revokePrivilegeTable = (usernameRev, tableRev, privilegeRev) => {
		try {
			const revokePrivilege = {
				p_username: usernameRev,
				p_table: tableRev,
                p_privilege: privilegeRev
			}

			fetch('http://localhost:8080/api/v1/privilege/revokeTable', {
				method: 'POST',
				body: JSON.stringify(revokePrivilege)
			})
			.then(response => {
				if(!response.ok) {
					console.log("Error when fetching !!!");
					return;
				}
				//success("Grant Previlege successfully !");
				return response.json();
			})
		}
		catch(err) {
			console.log(err);
		}
	}

    const setOptionPrivilegeTable = () => {
        var result = [];
        listPrivilege.map((item) => {
            if(item.TABLE_NAME == props.tableSelect)  {
                switch(item.PRIVILEGE) {
                    case "SELECT": {
                        result.push({value: "SELECT"});
                        break;
                    }
                    case "INSERT": {
                        result.push({value: "INSERT"});
                        break;
                    }
                    case "UPDATE": {
                        result.push({value: "UPDATE"});
                        break;
                    }
                    case "DELETE": {
                        result.push({value: "DELETE"});
                        break;
                    }
                    default: {
                        break;
                    }
                }
            }
        })
        setOptionTable(result);
    }

    const convertPrevilegeToString = () => {
		var str = ""
		listPrivilegeRevoke.map((item, index) => {
			if(index == (listPrivilegeRevoke.length - 1)) {
				str = str + item;
			}
			else {
				str = str + (item + ",");
			}
		})
		return str;
	}

    useEffect(() => {
        getListPrivilegeOfUser(props.userPrivilege);
    }, [props.userPrivilege])

    useEffect(() => {
        setOptionPrivilegeTable();
    }, [listPrivilege])
    
    return (
        <div>
            <Select
                onChange={(e) => setListPrivilegeRevoke(e)}
                mode="multiple"
                tagRender={tagRender}
                style={{
                    width: '100%',
                }}
                options={optionTable}
                />
            <Flex
                justify={'center'} >
                <Button 
                    style={{marginTop: '10px'}}
                    type="primary" danger
                    onClick={() => {
                        revokePrivilegeTable(props.userPrivilege, props.tableSelect, convertPrevilegeToString());
                        props.setOpen(false);
                    }} >
                    Revoke
                </Button>
            </Flex>
        </div>
    )
};

export default RevokeForm;
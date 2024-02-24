import React from "react";

const List = (props) => {
    const { listInfo, headers } = props;
    return(
        <table className="table">
        <thead>
            {headers.map((header, index) => (
                <th key={index}>{header}</th>
            ))}
        </thead>
        <tbody>
            {listInfo.map((item) => (
            <tr key={item.id}>
                {Object.values(item).map((value, index) => (
                    <td key={index}>{value == null ? "(NULL)" : value}</td>
                ))}
            </tr>
            ))}
        </tbody>
     </table>
    )
};

export default List;
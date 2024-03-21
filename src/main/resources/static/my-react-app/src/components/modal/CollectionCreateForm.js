import React, { useEffect } from "react";

import { Form } from 'antd';
import AddProfile from "../../pages/ProfilePage/AddProfile";
import RevokeForm from "../privilege/RevokeForm";

const CollectionCreateForm = ({ initialValues, onFormInstanceReady, typeForm, userPrivilege, tableSelect, setOpen }) => {
    const [form] = Form.useForm();
    useEffect(() => {
      onFormInstanceReady(form);
    }, []);
    return (
      <Form layout="vertical" form={form} name="form_in_modal">
        {/* { typeForm == "CreateForm" && <AddProfile setOpen={setOpen} success={success} setIsAdded={setIsAdded} />} */}
        { typeForm == "RevokeObjectUser" && <RevokeForm userPrivilege={userPrivilege} tableSelect={tableSelect} setOpen={setOpen} />}
      </Form>
    );
  };

  export default CollectionCreateForm;
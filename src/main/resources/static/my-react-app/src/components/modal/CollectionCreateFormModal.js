import React, { useState } from "react";
import { Modal } from 'antd';
import CollectionCreateForm from "./CollectionCreateForm";

const CollectionCreateFormModal = ({ open, onCancel, initialValues, type, titleModal, userPrivilege, tableSelect, setOpen }) => {
    const [formInstance, setFormInstance] = useState();
    return (
      <Modal
        open={open}
        title={titleModal}
        okText="Create"
        cancelText="Cancel"
        okButtonProps={{
          autoFocus: true,
        }}
        footer={[]}
        onCancel={onCancel}
        destroyOnClose
        onOk={async () => {
          try {
            const values = await formInstance?.validateFields();
            formInstance?.resetFields();
          } catch (error) {
            console.log('Failed:', error);
          }
        }}
      >
        <CollectionCreateForm
          initialValues={initialValues}
          onFormInstanceReady={(instance) => {
            setFormInstance(instance);
          }}
          typeForm={type}
          userPrivilege={userPrivilege}
          tableSelect={tableSelect}
          setOpen={setOpen}
        />
      </Modal>
    );
  };

  export default CollectionCreateFormModal;
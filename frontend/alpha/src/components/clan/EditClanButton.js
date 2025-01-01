import { Button } from "antd";
import {
    EditFilled
  } from "@ant-design/icons";


const EditClanButton = ( {showEditModal} ) => {
    return (
        <Button type="primary" icon={<EditFilled />} style={{ marginRight: "10px" }} onClick={showEditModal}>
            Editar clan
        </Button>
    );
}

export default EditClanButton;
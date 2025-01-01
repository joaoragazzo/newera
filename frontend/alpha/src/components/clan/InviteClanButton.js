import { Button } from "antd";
import {
    PlusOutlined
} from "@ant-design/icons";

const InviteClanButton = ( {showInviteModal} ) => {
    return (
        <Button type="primary" icon={<PlusOutlined />} onClick={showInviteModal} >
            Convidar novos membros
        </Button>
    );
}

export default InviteClanButton;
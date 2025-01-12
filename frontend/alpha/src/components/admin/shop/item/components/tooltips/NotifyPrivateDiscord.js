import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const NotifyPrivateDiscord = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Notificar webhook privado do Discord</span>
        <Tooltip
            title="Se o webhook privado estiver definido, a cada compra desse item, será enviado uma notificação para esse webhook."
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default NotifyPrivateDiscord;
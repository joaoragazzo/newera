import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const NotifyPublicDiscord = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Notificar webhook privado do Discord</span>
        <Tooltip
            title="Se o webhook público estiver definido, a cada compra desse item, será enviado uma notificação para esse webhook."
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default NotifyPublicDiscord;
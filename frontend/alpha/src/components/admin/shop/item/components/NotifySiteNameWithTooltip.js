import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const NotifySiteWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Notificar usuários</span>
        <Tooltip
            title="Notificar usuários do site desse novo item na loja. Também irá notificar os usuários a cada modificação do item"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default NotifySiteWithTooltip;
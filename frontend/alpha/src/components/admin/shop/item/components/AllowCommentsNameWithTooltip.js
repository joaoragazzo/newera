import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const AllowCommentsNameWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Permitir comentários</span>
        <Tooltip
            title="Permitir um único comentário de pessoas que já adquiriram o produto"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default AllowCommentsNameWithTooltip;
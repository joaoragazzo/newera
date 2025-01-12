import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const AllowReviewWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Permitir avaliações</span>
        <Tooltip
            title="Permitir avaliações, de 0 a 5 estrelas, para cada cada compra realizada"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default AllowReviewWithTooltip;
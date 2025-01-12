import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const TagsNameWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Tags associadas</span>
        <Tooltip
            title="As tags podem ser utilizadas futuramente em regras de promoções e cupons na loja"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default TagsNameWithTooltip;
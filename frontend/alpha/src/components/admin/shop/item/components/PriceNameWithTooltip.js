import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const PriceNameWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Valor</span>
        <Tooltip
            title="O valor a ser pago pelo produto. Você deve escolher se ele será de uso único, um benefício semanal, mensal, anual, permanente ou que resetará todo wipe"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default PriceNameWithTooltip;
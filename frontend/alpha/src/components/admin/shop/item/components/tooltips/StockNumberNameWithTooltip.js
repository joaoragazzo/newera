import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const StockNumberNameWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Número no estoque</span>
        <Tooltip
            title="Quantidade máxima de compras que esse item pode ter. 
                Para benefícios temporários, a cada vez que um benefício expirar, 
                o estoque será novamente renovado. Deixe em branco para um estoque
                infinito."
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default StockNumberNameWithTooltip;
import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const ShowAcquisitionWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Mostrar aquisições</span>
        <Tooltip
            title="Mostrar, públicamente, o número de aquisições desse produto"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default ShowAcquisitionWithTooltip;
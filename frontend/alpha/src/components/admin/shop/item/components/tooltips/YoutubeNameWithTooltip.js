import { Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const YoutubeNameWithTooltip = () => {
    return (<>
        <span style={{ marginRight: "5px" }}>Vídeos do Youtube</span>
        <Tooltip
            title="Os vídeos são opcionais e serão mostrados dentro da página do item"
            placement="bottom"
        >
            <QuestionCircleOutlined />
        </Tooltip>
    </>)
}

export default YoutubeNameWithTooltip;
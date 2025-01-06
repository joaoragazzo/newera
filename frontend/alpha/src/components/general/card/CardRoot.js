import { Card, Tooltip } from "antd";
import { QuestionCircleOutlined } from "@ant-design/icons";

const CardRoot = ({ cardTitle, cardToolTip, children }) => {
  return (
    <Card
      title={
        <>
          {cardTitle}
          {cardToolTip && (
            <Tooltip title={cardToolTip}>
              <QuestionCircleOutlined style={{ marginLeft: "10px" }} />
            </Tooltip>
          )}
        </>
      }
      style={{
        borderRadius: "8px",
        height: "100%",
      }}
    >
      {children}
    </Card>
  );
};

export default CardRoot;

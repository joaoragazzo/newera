import { Progress } from "antd";
import CardRoot from "../../general/card/CardRoot";

const ActiveMembersCard = () => {
    return (
        <CardRoot cardTitle={"Membros Ativos"} cardToolTip={"Este card exibe os membros que estão ativos no momento."}>
            <div
                style={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                }}
            >
                <Progress type="circle" percent={0} />
            </div>
        </CardRoot>
    );
}

export default ActiveMembersCard;
import { Progress } from "antd";
import CardRoot from "../../general/card/CardRoot";


const ActivityMembersCard = () => {
    return (
        <CardRoot cardTitle={"Taxa de atividade"} cardToolTip={"Esse card exibe a atividade do clan. A atividade é mesurada a dividindo a somatória do tempo online total do clan com o tempo total."}>
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

export default ActivityMembersCard;
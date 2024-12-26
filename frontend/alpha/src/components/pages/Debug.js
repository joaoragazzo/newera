import { Button, Card, Form, Input } from "antd";
import FormItem from "antd/es/form/FormItem";


const onFinish = (values) => {
    console.log(values)
};

const Debug = () => {
    return (
        <Card>
            <Form method="POST" action={"/api/debug/setsteam64id"} onFinish={onFinish} >
                <Form.Item name="steam64id" label="Steam64ID" rules={[{ required: true, message: "Provide the Steam64ID" }]}>
                    <Input placeholder="Set steam64id from a player"></Input>
                </Form.Item>
                <Button type="primary" htmlType="submit" >Definir</Button>
            </Form>
        </Card>

    );
}

export default Debug;
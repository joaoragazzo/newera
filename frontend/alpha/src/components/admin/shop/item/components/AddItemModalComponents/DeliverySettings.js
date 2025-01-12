import { Col, Form, Row, Switch } from "antd";




const DeliverySettings = () => {
    return (
        <Row>
            <Col span={12}>
                <Form.Item name="manualDelivery" label="Entrega manual">
                    <Switch />
                </Form.Item>
            </Col>
        </Row>
    );
}

export default DeliverySettings;
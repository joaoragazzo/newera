import { Col, Form, Row, Switch } from "antd"
import NotifySiteWithTooltip from "../tooltips/NotifySiteNameWithTooltip"
import NotifyPublicDiscord from "../tooltips/NotifyPublicDiscord"
import NotifyPrivateDiscord from "../tooltips/NotifyPrivateDiscord"


const NotificationSettings = () => {
    return (
        <Row>
            <Col span={12}>
                <Form.Item name="notifySite" label={<NotifySiteWithTooltip />} >
                    <Switch />
                </Form.Item>
            </Col>
            <Col span={12}>
                <Form.Item name="notifyPublicDiscord" label={<NotifyPublicDiscord />} >
                    <Switch />
                </Form.Item>
            </Col>
            <Col span={12}>
                <Form.Item name="notifyPrivateDiscord" label={<NotifyPrivateDiscord />} >
                    <Switch />
                </Form.Item>
            </Col>
        </Row>
    )
}

export default NotificationSettings
import { Col, Row, Switch, Form } from "antd"
import AllowCommentsNameWithTooltip from "../tooltips/AllowCommentsNameWithTooltip"
import AllowReviewWithTooltip from "../tooltips/AllowReviewNameWithTooltip"
import ShowAcquisitionWithTooltip from "../tooltips/ShowAcquisitionWithTooltip"


const InterationsSettings = () => {
    return (
        <Row>
            <Col span={12}>
                <Form.Item name="allowComments" label={<AllowCommentsNameWithTooltip />} >
                    <Switch />
                </Form.Item>
            </Col>
            <Col span={12}>
                <Form.Item name="allowRating" label={< AllowReviewWithTooltip />} >
                    <Switch />
                </Form.Item>
            </Col>
            <Col span={12}>
                <Form.Item name="showAcquisition" label={< ShowAcquisitionWithTooltip />} >
                    <Switch />
                </Form.Item>
            </Col>
        </Row>
    )
}

export default InterationsSettings;
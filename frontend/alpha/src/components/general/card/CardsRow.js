import { Col, Row } from "antd";
import React from "react";
import '../../../styles/Scroll.css';


const CardsRow = ({ children }) => {
    const childrenArray = React.Children.toArray(children);

    return (
        <Row gutter={[16, 16]} style={{ marginBottom: "20px", flexWrap: "nowrap" }}>
            {childrenArray.map((child, index) => (
                <Col span={8} key={index}>
                    {child}
                </Col>
            ))}
        </Row>
    );
}

export default CardsRow;
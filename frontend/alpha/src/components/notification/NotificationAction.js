import { Button } from "antd";

export function NotificationAction( { icon: Icon, callback, ...others } ) {
    return (
        <Button icon={< Icon />} onClick={callback} {...others} />
    );
}
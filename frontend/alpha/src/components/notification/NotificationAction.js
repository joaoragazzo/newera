import { Button, message } from "antd";
import axios from "axios";

export function NotificationAction( { icon: Icon, callback_url, ...others } ) {
    const callback_function = async () => {
        try {
            const response = await axios.post(callback_url)
            
            if (response.data.message) {
                message.success(response.data.message)
            }
        } catch (error) {
            message.error(error.response.data.error)
        }
    }
    
    return (
        <Button icon={< Icon />} onClick={callback_function} {...others} />
    );
}
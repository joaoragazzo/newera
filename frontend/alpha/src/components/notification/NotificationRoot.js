import { NotificationActions } from "./NotificationActions";

export function NotificationRoot( { children }) {
    return (
        <div
            style={{
                display: "flex",
                alignItems: "center",
                padding: "10px",
                borderRadius: "8px",
                marginBottom: "10px",
            }}
        >
            <div style={{ display: "flex", alignItems: "center", flex: 1 }}>
                {children.filter((child) => child.type !== NotificationActions)}
            </div>

            <div>
                {children.filter((child) => child.type === NotificationActions)}
            </div>
        </div>
    );
}

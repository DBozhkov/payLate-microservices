class MessageModel {
    userName: string;
    title: string;
    question: string;
    id?: number;
    userEmail?: string;
    adminEmail?: string;
    response?: string;
    closed?: boolean;

    constructor(userName: string, title: string, question: string){
        this.userName = userName;
        this.title = title;
        this.question = question;
    }
}

export default MessageModel;
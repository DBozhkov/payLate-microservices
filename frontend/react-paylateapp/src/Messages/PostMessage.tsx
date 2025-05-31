import { useOktaAuth } from '@okta/okta-react';
import { useState } from 'react';
import MessageModel from '../models/MessageModel';

export const PostMessage = () => {

    const { authState } = useOktaAuth();
    const [userName, setUserName] = useState('');
    const [title, setTitle] = useState('');
    const [question, setQuestion] = useState('');
    const [isAuthenticated, setIsAuthenticated] = useState(true);
    const [displayWarning, setDisplayWarning] = useState(false);
    const [displaySuccess, setDisplaySuccess] = useState(false);

    async function submitNewQuestion() {
        const url = `${process.env.REACT_APP_API}/messages/secure/add/message`;

        if (!authState?.isAuthenticated) {
            setIsAuthenticated(false);
            setDisplayWarning(true);
            setDisplaySuccess(false);

            return;
        } else if (isAuthenticated && title !== '' && question !== '' && userName !== '') {
            const messageRequestModel: MessageModel = new MessageModel(userName, title, question);
            const requestOptions = {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${authState?.accessToken?.accessToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(messageRequestModel)
            };

            const submitNewQuestionResponse = await fetch(url, requestOptions);
            if (!submitNewQuestionResponse.ok) {
                throw new Error('Something went wrong!');
            }

            setUserName('');
            setTitle('');
            setQuestion('');
            setDisplayWarning(false);
            setDisplaySuccess(true);
        } else {
            setDisplayWarning(true);
            setDisplaySuccess(false);
        }
    }

    return (
        <div className="row mb-4">
            <div className="col-12">
                <h2 className="fw-bold text-center">Write us a message</h2>
                {!isAuthenticated && (
                    <div className="alert alert-danger text-center" role="alert">
                        Please log in before writing a message.
                    </div>
                )}
                {isAuthenticated && displayWarning && (
                    <div className="alert alert-danger text-center" role="alert">
                        All fields must be filled out.
                    </div>
                )}
                {displaySuccess && (
                    <div className="alert alert-success text-center" role="alert">
                        Your question has been submitted successfully.
                    </div>
                )}
                <form className="p-4 border rounded shadow-sm bg-light">
                    <div className="row g-3">
                        <div className="col-md-6">
                            <label className="form-label fw-bold">
                                Name <span className="text-danger">*</span>
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Please enter your name"
                                onChange={(e) => setUserName(e.target.value)}
                                value={userName}
                                required
                            />
                        </div>


                        <div className="col-md-12">
                            <label className="form-label fw-bold">
                                Title <span className="text-danger">*</span>
                            </label>
                            <input
                                type="text"
                                className="form-control"
                                placeholder="Please enter a title"
                                onChange={(e) => setTitle(e.target.value)}
                                value={title}
                                required
                            />
                        </div>

                        <div className="col-md-12">
                            <label className="form-label fw-bold">
                                Question <span className="text-danger">*</span>
                            </label>
                            <textarea
                                className="form-control"
                                rows={4}
                                placeholder="Please enter your question"
                                onChange={(e) => setQuestion(e.target.value)}
                                value={question}
                                required
                            ></textarea>
                        </div>

                        <div className="col-12 text-center">
                            <button
                                type="button"
                                className="btn btn-primary"
                                onClick={submitNewQuestion}
                            >
                                Submit
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}
import React, { useEffect, useState } from 'react';
import { useOktaAuth } from '@okta/okta-react';
import { Navigate } from 'react-router-dom';

const ProfilePage: React.FC = () => {
    const { authState } = useOktaAuth();
    const [profile, setProfile] = useState<any>({});
    const [editing, setEditing] = useState(false);
    const [updatedProfile, setUpdatedProfile] = useState<any>({});
    const [saving, setSaving] = useState(false);

    useEffect(() => {
        if (authState?.isAuthenticated) {
            const fetchUserProfile = async () => {
                try {
                    const response = await fetch(`/api/user-account/${authState.idToken?.claims.sub}`, {
                        headers: {
                            Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                        },
                    });

                    if (!response.ok) {
                        throw new Error('Failed to fetch user profile');
                    }

                    const userProfile = await response.json();
                    setProfile(userProfile);
                    setUpdatedProfile({
                        firstName: userProfile.firstName || '',
                        lastName: userProfile.lastName || '',
                        middleName: userProfile.middleName || '',
                        city: userProfile.city || '',
                        zipCode: userProfile.zipCode || '',
                        nickName: userProfile.nickName || '',
                        mobilePhone: userProfile.mobilePhone || '',
                        postalAddress: userProfile.postalAddress || '',
                    });
                } catch (error) {
                    console.error('Error fetching user profile:', error);
                }
            };

            fetchUserProfile();
        }
    }, [authState]);

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const { name, value } = e.target;
        setUpdatedProfile({ ...updatedProfile, [name]: value });
    };

    const handleSave = async () => {
        if (!authState || !authState.accessToken) {
            console.error('Access token is missing');
            return;
        }

        setSaving(true);
        try {
            const response = await fetch('/api/user-account', {
                method: 'POST',
                headers: {
                    Authorization: `Bearer ${authState.accessToken?.accessToken}`,
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    oktaUserId: authState.idToken?.claims.sub,
                    firstName: updatedProfile.firstName,
                    lastName: updatedProfile.lastName,
                    middleName: updatedProfile.middleName,
                    city: updatedProfile.city,
                    zipCode: updatedProfile.zipCode,
                    nickName: updatedProfile.nickName,
                    mobilePhone: updatedProfile.mobilePhone,
                    postalAddress: updatedProfile.postalAddress,
                }),
            });

            if (!response.ok) {
                throw new Error(`Failed to save profile: ${response.status}`);
            }

            const updatedUser = await response.json();
            setProfile(updatedUser);
            setEditing(false);
        } catch (error) {
            console.error('Error saving profile:', error);
        } finally {
            setSaving(false);
        }
    };

    if (!authState?.isAuthenticated) {
        return <Navigate to='/login' />;
    }

    return (
        <div className='container mt-5'>
            <h2>Profile</h2>
            <div className='card p-4'>
                {!editing ? (
                    <>
                        <p><strong>First Name:</strong> {profile.firstName || 'N/A'}</p>
                        <p><strong>Last Name:</strong> {profile.lastName || 'N/A'}</p>
                        <p><strong>Middle Name:</strong> {profile.middleName || 'N/A'}</p>
                        <p><strong>City:</strong> {profile.city || 'N/A'}</p>
                        <p><strong>Zip Code:</strong> {profile.zipCode || 'N/A'}</p>
                        <p><strong>Nickname:</strong> {profile.nickName || 'N/A'}</p>
                        <p><strong>Mobile Phone:</strong> {profile.mobilePhone || 'N/A'}</p>
                        <p><strong>Postal Address:</strong> {profile.postalAddress || 'N/A'}</p>
                        <button className='btn btn-primary' onClick={() => setEditing(true)}>Edit Profile</button>
                    </>
                ) : (
                    <>
                        <div className='mb-3'>
                            <label htmlFor='firstName' className='form-label'>First Name</label>
                            <input
                                type='text'
                                className='form-control'
                                id='firstName'
                                name='firstName'
                                value={updatedProfile.firstName || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='lastName' className='form-label'>Last Name</label>
                            <input
                                type='text'
                                className='form-control'
                                id='lastName'
                                name='lastName'
                                value={updatedProfile.lastName || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='middleName' className='form-label'>Middle Name</label>
                            <input
                                type='text'
                                className='form-control'
                                id='middleName'
                                name='middleName'
                                value={updatedProfile.middleName || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='city' className='form-label'>City</label>
                            <input
                                type='text'
                                className='form-control'
                                id='city'
                                name='city'
                                value={updatedProfile.city || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='zipCode' className='form-label'>Zip Code</label>
                            <input
                                type='text'
                                className='form-control'
                                id='zipCode'
                                name='zipCode'
                                value={updatedProfile.zipCode || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='nickName' className='form-label'>Nickname</label>
                            <input
                                type='text'
                                className='form-control'
                                id='nickName'
                                name='nickName'
                                value={updatedProfile.nickName || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='mobilePhone' className='form-label'>Mobile Phone</label>
                            <input
                                type='text'
                                className='form-control'
                                id='mobilePhone'
                                name='mobilePhone'
                                value={updatedProfile.mobilePhone || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <div className='mb-3'>
                            <label htmlFor='postalAddress' className='form-label'>Postal Address</label>
                            <input
                                type='text'
                                className='form-control'
                                id='postalAddress'
                                name='postalAddress'
                                value={updatedProfile.postalAddress || ''}
                                onChange={handleInputChange}
                            />
                        </div>
                        <button className='btn btn-success me-2' onClick={handleSave} disabled={saving}>
                            {saving ? 'Saving...' : 'Save'}
                        </button>
                        <button className='btn btn-secondary' onClick={() => setEditing(false)}>Cancel</button>
                    </>
                )}
            </div>
        </div>
    );
};

export default ProfilePage;
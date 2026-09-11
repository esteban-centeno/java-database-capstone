# Schema Design

## MySQL Database Design

### Table: patients
- id: INT, Primary Key, Auto Increment
- full_name: varchar, Not Null
- last_name: varchar, Not Null
- user_name: varchar, Not Null
- password: varchar, Not Null
- email: varchar, Not Null
- phone: varchar

### Table: doctors
- id: INT, Primary Key, Auto Increment
- full_name: varchar, Not Null
- last_name: varchar, Not Null
- user_name: varchar, Not Null, Unique
- password: varchar, Not Null
- email: varchar, Not Null, Unique
- phone: varchar

### Table: appointment
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key → doctors(id)
- patient_id: INT, Foreign Key → patients(id)
- appointment_time: DATETIME, Not Null
- appointment_duration: INT
- status: INT (0 = Scheduled, 1 = Completed, 2 = Cancelled)

### Table: admin
- id: INT, Primary Key, Auto Increment
- full_name: varchar, Not Null
- last_name: varchar, Not Null
- user_name: varchar, Not Null
- password: varchar, Not Null

### Table: availability_schedule
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key -> doctors(id)
- day_of_week: tinyint, (0-6 Sunday-Saturday)
- start_time: time
- end_time: time
- active: boolean

### Table: availability_override
- id: INT, Primary Key, Auto Increment
- doctor_id: INT, Foreign Key -> doctors(id)
- date: DATE, NOT NULL
- start_time: time, Nullable
- end_time: time, Nullable
- active: boolean

## MongoDB Collection Design

### Collection: prescriptions
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientName": "John Smith",
  "appointmentId": 51,
  "medication": "Paracetamol",
  "dosage": "500mg",
  "doctorNotes": "Take 1 tablet every 6 hours.",
  "refillCount": 2,
  "pharmacy": {
    "name": "Walgreens SF",
    "location": "Market Street"
  }
}
```

### Collection: feedback
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 123456,
  "doctorId": 123456
  "appointmentId": 51,
  "comments": "Excellent!",
  "rating": 5"
}
```

### Collection: logs
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 123456,
  "doctorId": 123456
  "appointmentId": 51,
  "symptoms": [
    "cough",
    "running nose"
  ],
  "observations": ["anxious"]
}
```

### Collection: messages
```json
{
  "_id": "ObjectId('64abc123456')",
  "patientId": 123456,
  "doctorId": 123456,
  "priority": "high",
  "message": "The medication is not working as expected"
}
```

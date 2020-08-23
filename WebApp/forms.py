from flask_wtf import FlaskForm
from wtforms import StringField, BooleanField, SubmitField, SelectField
from wtforms.validators import data_required, Optional, ValidationError
from wtforms.fields.html5 import DateField
from datetime import date, timedelta


class FlightsForm(FlaskForm):
    direction = SelectField(
        'Direction',
        validators=[data_required()],
        choices=[
            ("all", "all"),
            ("departures", "departures"),
            ("arrivals", "arrivals")
        ]
    )
    country = StringField('Country')
    city = StringField('City')
    airport = StringField('Airport')
    airline = StringField('Airline')
    date1 = DateField('Start Date', default=date.today(), validators=[Optional()])
    date2 = DateField('End Date', default=date.today() + timedelta(100), validators=[Optional()])
    sunday = BooleanField('Sunday')
    monday = BooleanField('Monday')
    tuesday = BooleanField('Tuesday')
    wednesday = BooleanField('Wednesday')
    thursday = BooleanField('Thursday')
    friday = BooleanField('Friday')
    saturday = BooleanField('Saturday')

    submit = SubmitField('Show Filtered Flights')

    def validate_date1(self, field):
        if field.data < date.today():
            raise ValidationError("Start date must not be earlier than today.")

    def validate_date2(self, field):
        if field.data > date.today() + timedelta(120):
            raise ValidationError("Start date must not be too far in the future.")
FROM python:3.8
WORKDIR /data

COPY . .

RUN pip install -r requirements.txt
WORKDIR /data/

ENTRYPOINT ["top", "-b"]
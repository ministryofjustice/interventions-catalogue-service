{{/* vim: set filetype=mustache: */}}
{{/*
Environment variables for web and worker containers
*/}}
{{- define "deployment.envs" }}
env:
  - name: DB_PASS
    valueFrom:
      secretKeyRef:
        name: interventions-catalogue-rds-instance-output
        key: database_password

  - name: DB_USER
    valueFrom:
      secretKeyRef:
        name: interventions-catalogue-rds-instance-output
        key: database_username

  - name: DB_SERVER
    valueFrom:
      secretKeyRef:
        name: interventions-catalogue-rds-instance-output
        key: rds_instance_address

  - name: DB_NAME
    valueFrom:
      secretKeyRef:
        name: interventions-catalogue-rds-instance-output
        key: database_name

  - name: DB_SSLMODE
    value: "verify-full"

  - name: NOMIS_AUTH_URL
    value: {{ .Values.env.NOMIS_AUTH_URL | quote }}

  - name: NOMIS_OAUTH_PUBLIC_KEY
    value: {{ .Values.env.NOMIS_OAUTH_PUBLIC_KEY | quote }}
{{- end -}}

#!/bin/bash

source ./../commons.sh

COMPONENTS_CSV="./../../../application/components.csv"

validate_operation() {
  local operation=$1

  local valid_operations=("template" "install" "uninstall")

  for valid_operation in "${valid_operations[@]}"; do
    if [[ "$operation" == "$valid_operation" ]]; then
      return 0
    fi
  done

  echo "El valor de la operación debe coincidir con: ${valid_operations[*]}"
  return 1
}

iterate_csv_records() {
  local operation=$1

  validate_operation "$operation"

  firstline=true
  while IFS=',' read -r component_name component_type || [ -n "$component_name" ]; do
    # Ignore headers
    if $firstline; then
        firstline=false
        continue
    fi

    # Ignore comments
    if [[ $component_name != "#"* ]] && [[ $component_type != "commons" ]] ; then
      ./helm-record-processor.sh "$operation" "$component_name" "$component_type"
    fi

  done < <(sed 's/\r//g' "$COMPONENTS_CSV")
}

operation=$1
iterate_csv_records "$operation"